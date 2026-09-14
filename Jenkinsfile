pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '20'))
    timeout(time: 45, unit: 'MINUTES')
  }

  environment {
    EXECUTION_ENV = 'remote'
    HEADLESS = 'true'
    SELENIUM_REMOTE_URL = 'http://selenium-hub:4444/wd/hub'
    DOCKER_NETWORK = 'tninja_ci-net'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Ensure Selenium Grid') {
      steps {
        sh '''
          set -e
          if ! curl -sf "${SELENIUM_REMOTE_URL%/wd/hub}/status" >/dev/null 2>&1 \
             && ! curl -sf "http://selenium-hub:4444/status" >/dev/null 2>&1; then
            echo "Grid not reachable on compose network — starting docker-compose.grid.yml"
            docker compose -f docker-compose.grid.yml up -d
            for i in $(seq 1 40); do
              if curl -sf http://localhost:4444/status >/dev/null; then
                echo "Local grid is up"
                break
              fi
              sleep 3
            done
          else
            echo "Selenium Grid already available"
          fi
        '''
      }
    }

    stage('Build test image') {
      steps {
        sh 'docker build -t tutorialsninja-tests:ci .'
      }
    }

    stage('Run CI suite') {
      steps {
        sh '''
          set -e
          mkdir -p reports target
          # Prefer shared compose network; fall back to host gateway URL
          NET_ARGS=""
          if docker network inspect "$DOCKER_NETWORK" >/dev/null 2>&1; then
            NET_ARGS="--network $DOCKER_NETWORK"
            GRID_URL="http://selenium-hub:4444/wd/hub"
          else
            NET_ARGS="--network host"
            GRID_URL="http://localhost:4444/wd/hub"
          fi

          docker run --rm $NET_ARGS \
            -e EXECUTION_ENV=remote \
            -e HEADLESS=true \
            -e SELENIUM_REMOTE_URL="$GRID_URL" \
            -v "$PWD/reports:/workspace/reports" \
            -v "$PWD/target:/workspace/target" \
            tutorialsninja-tests:ci \
            mvn -B -Pci test \
              -Dexecution_env=remote \
              -Dselenium.grid.url="$GRID_URL" \
              -Dheadless=true \
              -DsuiteXmlFile=src/test/java/testBase/ci-suite.xml
        '''
      }
    }

    stage('Publish Extent Reports') {
      steps {
        archiveArtifacts artifacts: 'reports/**/*.html,target/surefire-reports/**', allowEmptyArchive: true
        script {
          if (fileExists('reports')) {
            publishHTML(target: [
              allowMissing: true,
              alwaysLinkToLastBuild: true,
              keepAll: true,
              reportDir: 'reports',
              reportFiles: '*.html',
              reportName: 'Extent Report'
            ])
          }
        }
      }
    }
  }

  post {
    success {
      echo 'CI pipeline passed — regression suite completed on Selenium Grid.'
    }
    failure {
      echo 'CI pipeline failed — inspect console output and archived Extent reports.'
    }
  }
}
