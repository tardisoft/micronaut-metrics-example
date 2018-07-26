#!groovy
@Library('jenkins-enterprise@master')
import java.lang.Object

buildApplication {
    notifySteps = [new FooNotifituer()]
}

class FooNotifituer {

    def call(def script){
        script.echo "doing work"
    }

}