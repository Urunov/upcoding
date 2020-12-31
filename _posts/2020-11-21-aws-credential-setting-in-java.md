---
title: AWS Credentials in Java (Custom AWS Credential Provider Chain)
categories:
 - java, aws, github
tags:
 - aws, cloud, java, spring boot, spring, credential, credential provider chain, custom, github, github actions
---

In order to use AWS cloud resources from application we have to setup connection with specific account. AWS SDK is supported by dozens of programming languages and JAVA is one of them. In order to setup connection the concept of credential provider chain must be understood. There are multiple ways of using AWS Credential through the application (Example: Through environment variables, java system properties, web identity token, etc). AWS SDK allows us to use one or multiple of these ways together in order to setup connection. It means we can build a chain out of these possible ways and the chain works as a fallback. Means if the first way of connection fails, SDK will try the second way, and then third and so on. 

AWS SDK provides us with a [Default Credential Provider Chain](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html) which has a following order:
- **Environment variables** – AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY. The AWS SDK for Java uses the EnvironmentVariableCredentialsProvider class to load these credentials.
- **Java system properties** – aws.accessKeyId and aws.secretKey. The AWS SDK for Java uses the SystemPropertiesCredentialsProvider to load these credentials.
- **Web Identity Token credentials** from the environment or container.
- **The default credential profiles file** – typically located at ~/.aws/credentials (location can vary per platform), and shared by many of the AWS SDKs and by the AWS CLI. The AWS SDK for Java uses the ProfileCredentialsProvider to load these credentials. You can create a credentials file by using the aws configure command provided by the AWS CLI, or you can create it by editing the file with a text editor. For information about the credentials file format, see AWS Credentials File Format.
- **Amazon ECS container credentials** - loaded from the Amazon ECS if the environment variable AWS_CONTAINER_CREDENTIALS_RELATIVE_URI is set. The AWS SDK for Java uses the ContainerCredentialsProvider to load these credentials. You can specify the IP address for this value.
- **Instance profile credentials** – used on EC2 instances, and delivered through the Amazon EC2 metadata service. The AWS SDK for Java uses the InstanceProfileCredentialsProvider to load these credentials. You can specify the IP address for this value.

What that order basically means: first it will try system environment variables, if can't find AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY, then goes for java system properties. In Spring (or Spring Boot) it will be `.properties` or `.yaml` files where the properties are stored. After that it tries to check the Web Identity token from the environment, after that it checks the profile files which is located at `~/.aws/credentials`, and so on.

## Custom Credential Provider Chain
But what if we want to change the order of these credential methods order. Specifically lets say we want to check the profile files first and as a fallback we want to go for environment variables. Here is how I'm defining to check `~/.aws/credentials` first and look inside for profile with name "rustam-aws-profile". If it fails then just follow default credential provider chain, which will check the environment.

```java
public AWSCredentialsProvider amazonAWSCredentialsProvider() {
    List<AWSCredentialsProvider> providers = new ArrayList<>();

    providers.add(new ProfileCredentialsProvider("rustam-aws-profile"));
    providers.add(new DefaultAWSCredentialsProviderChain());

    return new AWSCredentialsProviderChain(
            providers.toArray(new AWSCredentialsProvider[providers.size()]));
}
```

Note: if You have spotted the code above will check `~/.aws/credentials` file twise, first time looking for profile "rustam-aws-profile", second time just looking for "default" profile.

Here is the full code for Spring Boot aws connection with DynamoDB:

```java
@Configuration
@EnableDynamoDBRepositories
public class DynamoDBConfig {
    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        List<AWSCredentialsProvider> providers = new ArrayList<>();
        providers.add(new ProfileCredentialsProvider("rustam-aws-profile"));
        providers.add(new DefaultAWSCredentialsProviderChain());
        return new AWSCredentialsProviderChain(
                providers.toArray(new AWSCredentialsProvider[providers.size()]));
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(amazonAWSCredentialsProvider())
                .withRegion(Regions.AP_NORTHEAST_2).build();
    }
}
```

## Where we may use it ?

It depends on our needs, but I needed it when start using Github Actions. As a most developers I have a multiple aws credentials on my local machine, and I separate them with profiles:

```
[default]
aws_access_key_id = XXXXXXXXXX
aws_secret_access_key = XXXXXXXXXXXXX

[aws-ac3-cli]
aws_access_key_id = XXXXXXXXXX
aws_secret_access_key = XXXXXXXXXXXXX

[aws-ac2-cli]
aws_access_key_id = XXXXXXXXXX
aws_secret_access_key = XXXXXXXXXXXXX

[aws-ac1-cli]
aws_access_key_id = XXXXXXXXXX
aws_secret_access_key = XXXXXXXXXXXXX

[aws-dna-cli]
aws_access_key_id = XXXXXXXXXX
aws_secret_access_key = XXXXXXXXXXXXX

[rustam-aws-profile]
aws_access_key_id = XXXXXXXXXX
aws_secret_access_key = XXXXXXXXXXXXX
```

Thats why when I run the code locally or debug I really want to use profile based credential activation. It could be good to have the profile based credential in github actions, but they don't support it [yet](https://github.com/aws-actions/configure-aws-credentials/issues/27), here is the closing statement from contributor regarding to the issue:

```
Closing this. This action intentionally does not write credentials to disk in the GitHub Actions environment, so the action does not configure profiles. As a workaround, you can use the environment variables it configures to configure profiles manually.

AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_SESSION_TOKEN
```

Using example above with custom credential provider chain I was able to realize my need.
