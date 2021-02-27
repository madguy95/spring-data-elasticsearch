FROM docker.elastic.co/logstash/logstash:7.3.2
MAINTAINER <tinhnx@gmail.com> Tinh Nguyen

# install dependency
RUN /usr/share/logstash/bin/logstash-plugin install logstash-input-jdbc
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-aggregate
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-jdbc_streaming
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-mutate

# copy lib database jdbc jars
COPY ./postgresql-42.2.8.jar /usr/share/logstash/logstash-core/lib/jars/postgresql.jar