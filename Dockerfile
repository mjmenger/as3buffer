FROM jenkins/jenkins:2.287
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
ENV CASC_JENKINS_CONFIG /var/jenkins_home/casc.yaml
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
COPY casc.yaml /var/jenkins_home/casc.yaml
COPY as3buffer.groovy /usr/local/as3buffer.groovy
COPY icrestbuffer.groovy /usr/local/icrestbuffer.groovy