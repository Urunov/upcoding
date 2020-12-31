---
title: Aws Lambda Usage of Environment Variables
categories:
 - aws
tags:
 - Node.js 12.x, PATH, variable, Error, bootstrap, runtime
---

One should be careful when using AWS lambda with environment variables. If you don't pay attention you may end-up wondering why your function is not working as expected. I personally had an issue of using PATH variable, if say it more exactly `process.environment.PATH` with runtime: Node.js 12.x. Because in version Node.js 10.x you won't face this problem.

In Node.js 12.x, You cannot declare environment variable PATH and assign some values to it, because somehow underlined OS uses this variable and your micro instance will be having trouble to execute and even load. As a result you will get following type of error at cloudwatch:

```python
2020-05-28T11:41:33.373+09:00
/var/runtime/bootstrap: line 14: expr: command not found
2020-05-28T11:41:33.374+09:00
/var/runtime/bootstrap: line 15: expr: command not found
2020-05-28T11:41:33.393+09:00
/var/runtime/bootstrap: line 16: expr: command not found
2020-05-28T11:41:33.413+09:00
/var/runtime/bootstrap: line 27: exec: env: not found
2020-05-28T11:41:33.445+09:00
END RequestId: f620XXXX-3891-XXXX-8c9b-XXXXbda2e656
2020-05-28T11:41:33.445+09:00
REPORT RequestId: f620XXXX-3891-XXXX-8c9b-XXXXbda2e656 Duration: 80.59 ms Billed Duration: 100 ms Memory Size: 128 MB Max Memory Used: 6 MB
2020-05-28T11:41:33.445+09:00
```

Resolution: just change the Environment variable that is called PATH to something else. (For example MY_PATH)

After facing this problem I have started wondering what are the other environment variables AWS Lambda has. Here some short lists:

## Reserved environment variables:

```
_HANDLER – The handler location configured on the function.

AWS_REGION – The AWS Region where the Lambda function is executed.

AWS_EXECUTION_ENV – The runtime identifier, prefixed by AWS_Lambda_—for example, AWS_Lambda_java8.

AWS_LAMBDA_FUNCTION_NAME – The name of the function.

AWS_LAMBDA_FUNCTION_MEMORY_SIZE – The amount of memory available to the function in MB.

AWS_LAMBDA_FUNCTION_VERSION – The version of the function being executed.

AWS_LAMBDA_LOG_GROUP_NAME, AWS_LAMBDA_LOG_STREAM_NAME – The name of the Amazon CloudWatch Logs group and stream for the function.

AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_SESSION_TOKEN – The access keys obtained from the function's execution role.

AWS_LAMBDA_RUNTIME_API – (Custom runtime) The host and port of the runtime API.

LAMBDA_TASK_ROOT – The path to your Lambda function code.

LAMBDA_RUNTIME_DIR – The path to runtime libraries.

TZ – The environment's time zone (UTC). The execution environment uses NTP to synchronize the system clock.
```

AWS documentation says the followings are additional environment variables that aren't reserved and can be extended in your function configuration. That means we can use them and even change them.

## Unreserved environment variables

```
LANG – The locale of the runtime (en_US.UTF-8).

PATH – The execution path (/usr/local/bin:/usr/bin/:/bin:/opt/bin).

LD_LIBRARY_PATH – The system library path (/lib64:/usr/lib64:$LAMBDA_RUNTIME_DIR:$LAMBDA_RUNTIME_DIR/lib:$LAMBDA_TASK_ROOT:$LAMBDA_TASK_ROOT/lib:/opt/lib).

NODE_PATH – (Node.js) The Node.js library path (/opt/nodejs/node12/node_modules/:/opt/nodejs/node_modules:$LAMBDA_RUNTIME_DIR/node_modules).

PYTHONPATH – (Python 2.7, 3.6, 3.8) The Python library path ($LAMBDA_RUNTIME_DIR).

GEM_PATH – (Ruby) The Ruby library path ($LAMBDA_TASK_ROOT/vendor/bundle/ruby/2.5.0:/opt/ruby/gems/2.5.0).

_X_AMZN_TRACE_ID – The X-Ray tracing header.

AWS_XRAY_CONTEXT_MISSING – For X-Ray tracing, Lambda sets this to LOG_ERROR to avoid throwing runtime errors from the X-Ray SDK.

AWS_XRAY_DAEMON_ADDRESS – For X-Ray tracing, the IP address and port of the X-Ray daemon.
```

Another concern should be about rewriting those environment variables. May be in some part of our code some libraries are using those variables, and rewriting environment wariables may cause errors in our lambda or even security issues. Here I have rewrited some of these environment variables (reserved and unreserved) and so far it is totally allowed.

```
exports.handler = async (event, context) => {
    // TODO implement
    process.env.PATH += ':/home/rustam';
    process.env.LAMBDA_RUNTIME_DIR += '/rustam';
    const response = {
        statusCode: 200,
        body: JSON.stringify('Hello from Lambda! ' + process.env.PATH ),
        bodyExtent: JSON.stringify('LAMBDA_RUNTIME_DIR: ' + process.env.LAMBDA_RUNTIME_DIR),
        TZ: JSON.stringify('TZ: ' + process.env.TZ),
        AWS_EXECUTION_ENV: JSON.stringify('AWS_EXECUTION_ENV: ' + process.env.AWS_EXECUTION_ENV),
    };
    return response;
};
```
