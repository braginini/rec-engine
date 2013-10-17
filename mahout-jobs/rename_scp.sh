sudo mvn clean compile assembly:single
sudo mv /home/mikhail/dev/IdeaProjects/recommendations/mahout-jobs/target/mahout-jobs-jar-with-dependencies.jar /home/mikhail/dev/IdeaProjects/recommendations/mahout-jobs/target/job.jar
sudo scp /home/mikhail/dev/IdeaProjects/recommendations/mahout-jobs/target/job.jar cloudera@localhost.localdomain:
