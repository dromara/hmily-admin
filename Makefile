HMILY_HOME :=  "."
REGISTRY ?= "docker.io"
REPOSITORY_PREF ?= "dromara/hmily"
ADMIN_REPOSITORY ?= "${REPOSITORY_PREF}-admin"
TAG ?= latest
VERSION ?= "1.0.2"
COMMIT_ID := $(shell git rev-parse HEAD)


default: build-hmily-admin-image

build-hmily-admin-image: build-hmily-admin
	@echo "build-hmily-admin-image"
	@docker buildx build --load \
    		-t ${REGISTRY}/${ADMIN_REPOSITORY}:${TAG} \
    		-f ${HMILY_HOME}/hmily-admin-dist/docker/Dockerfile \
    		--build-arg APP_NAME=hmily-admin-${VERSION}-admin-bin \
    		--label "commit.id=${COMMIT_ID}" \
    		${HMILY_HOME}/hmily-admin-dist

build-hmily-admin:
	@echo "build hmily admin"
	@mvn -am \
		-pl hmily-admin-dist \
		-Dmaven.javadoc.skip=true \
		-Drat.skip=true \
		-Djacoco.skip=true \
		-DskipTests \
		-Prelease \
		clean package