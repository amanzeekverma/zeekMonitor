javac -cp .:../WebContent/WEB-INF/lib/jcommon-0.9.6.jar:../WebContent/WEB-INF/lib/jfreechart-0.9.21.jar:../WebContent/WEB-INF/lib/servlet-api.jar ChartProcessor.java
java -cp jcommon-0.9.6.jar:jfreechart-0.9.21.jar:. ChartProcessor ../chart.properties 1> chart.log 2>chart.log &


