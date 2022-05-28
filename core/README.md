# crumbs - org.crumbs.core
simple java DI framework

System properties:
-----------------

| Property name | value example | Description |
| ----------- | ------------- | ----------------|
| `log.level` | `DEBUG`, `INFO`, `ERROR`, `WARN` | Logging level
| `log.packages` | `org.crumbs,com.gigi`| Comma separated package names to be logged |
| `log.dirPath` | `/var/log/sample`, `/C:/temp/somedir`| If specified, all logs go in dir path, else logs are outputed to stdout |
| `log.filePrefix` | `sampleapp`| Customize prefix of log files. Default is the name of the log dir defined |
| `log.daysBack` | `20`| The max number of days for logs to be kept until deleted |