#!/usr/bin/env bash

if ! grep -q letsencrypt </etc/nginx/nginx.conf; then
  sudo certbot -n -d "project-g7-env.eu-west-1.elasticbeanstalk.com" --nginx --agree-tos --email "cihatgelir35@gmail.com"
fi