# jenkins-shared-libraries
A collection of useful Jenkins global shared libraries to use with Jenkins Pipelines

To learn more on how you may use global shared libraries with jenkins:

[Documentation](https://jenkins.io/doc/book/pipeline/shared-libraries/)

### Using pipeline file parameters workaround given : https://issues.jenkins-ci.org/browse/JENKINS-27413

Usage with a declarative pipeline

```
pipeline {
    agent any
    parameters {
      file description: 'desc', name: 'someFolder/someFile.txt'
      file description: 'desc2', name: 'someFolder/anotherfile.txt'
      file description: 'desc3', name: 'filewithoutFolder.txt'
    }
    stages {
        stage('CheckFolder') {
            steps {
                pipelineFileParameter 'someFolder/someFile.txt'
                pipelineFileParameter 'someFolder/anotherfile.txt' 
                pipelineFileParameter 'filewithoutFolder.txt'
                sh "pwd"
                sh "ls -lah"
                sh "cat filewithoutFolder.txt"
                sh "ls -lah someFolder"
                sh "cat someFolder/someFile.txt"
                sh "cat someFolder/anotherfile.txt"
                //cleanup
                deleteDir()
            }
        }
    }
}
```

Above assumes an implicitly loaded library.

If you want to control import of library, given this library only makes use of scripts under /vars

Import it like below 

```
// Where 'fileParameter' is an example of the name given to the library when configuring Global Shared Library
// https://jenkins.io/doc/book/pipeline/shared-libraries/#using-libraries
library 'fileParameter'
pipeline {
    ...
}
```