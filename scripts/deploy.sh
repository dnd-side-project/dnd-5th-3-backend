cd /home/ubuntu/app/zip/
sudo docker-compose down
sudo docker build -t wtb:0.0 ./
sudo docker-compose up -d