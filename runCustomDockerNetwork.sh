#!/bin/bash

# Configuration: Modify SUBNET and CONTAINER_IP here to test different IP ranges
SUBNET="${1:-172.28.0.0/16}"
CONTAINER_IP="${2:-172.28.0.50}"
NETWORK_NAME="benchmark_custom_net"

echo "=========================================================="
echo " Starting OWASP Benchmark Container on Custom Subnet"
echo " Subnet:       $SUBNET"
echo " Container IP: $CONTAINER_IP"
echo "=========================================================="

# Check if image 'benchmark' exists locally, if not build it automatically
if ! docker image inspect benchmark >/dev/null 2>&1; then
    echo "Image 'benchmark' not found locally. Building Docker image from VMs/Dockerfile..."
    docker build -t benchmark -f VMs/Dockerfile .
fi

# Create custom network if it doesn't exist
docker network create --subnet=$SUBNET $NETWORK_NAME 2>/dev/null || true

# Run container with mounted local codebase
docker run -it --rm \
  --name owasp-benchmark-app \
  --network $NETWORK_NAME \
  --ip $CONTAINER_IP \
  -p 8443:8443 \
  -v "$(pwd)":/owasp/BenchmarkJava \
  benchmark /bin/bash -c "./runRemoteAccessibleBenchmark.sh"
