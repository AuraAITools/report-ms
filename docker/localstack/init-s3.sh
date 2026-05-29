#!/usr/bin/env bash
# Bootstraps the dev S3 bucket inside LocalStack. LocalStack runs every script
# in /etc/localstack/init/ready.d once the container is healthy.
set -euo pipefail

BUCKET="${AWS_S3_BUCKET_NAME:-aura-dev}"
REGION="${AWS_DEFAULT_REGION:-ap-southeast-1}"

echo "[init-s3] Creating bucket s3://${BUCKET} in ${REGION}"
awslocal s3api create-bucket \
    --bucket "${BUCKET}" \
    --region "${REGION}" \
    --create-bucket-configuration LocationConstraint="${REGION}" \
    2>/dev/null || echo "[init-s3] Bucket ${BUCKET} already exists"

echo "[init-s3] Buckets now present:"
awslocal s3 ls
