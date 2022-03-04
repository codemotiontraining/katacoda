#! /bin/bash

echo "Preparing the environment..."

curl -s https://raw.githubusercontent.com/rancher/k3d/main/install.sh | bash
snap install kubectl --classic
source ~/.bashrc
