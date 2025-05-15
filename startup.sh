#!/bin/sh
projects="authservice gateway resourceserver"
parallel=false

# Check for -p flag
if [ "$1" = "-p" ]; then
  parallel=true
fi

for project in $projects; do
  echo "Building image for $project..."

  if [ "$parallel" = true ]; then
    (cd "$project" && mvn spring-boot:build-image) &
  else
    (cd "$project" && mvn spring-boot:build-image)
  fi
done

# If running in parallel, wait for all background jobs to finish
if [ "$parallel" = true ]; then
  wait
fi

# Start Docker Compose
docker-compose up -d
