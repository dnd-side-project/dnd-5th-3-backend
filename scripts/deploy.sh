cd /home/ubuntu/app/zip/
sudo docker build -t wtb:0.0 ./
cd ../
sudo docker-compose down
sudo docker-compose up