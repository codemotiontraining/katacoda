#! /bin/bash
export TAG='v4.4.0'
curl -s https://raw.githubusercontent.com/rancher/k3d/main/install.sh | bash
snap install kubectl --classic
