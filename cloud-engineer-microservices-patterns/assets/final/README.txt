#### TEST DEL SISTEMA


docker pull codemotiontraining/microservices-product-service
docker pull codemotiontraining/microservices-shop-service
docker pull codemotiontraining/microservices-eureka-server
docker pull codemotiontraining/microservices-zuul-proxy

docker run --rm -d --network host -e PORT=8761 --name eureka-server codemotiontraining/microservices-eureka-server 

docker run --rm -d --network host -e PORT=8085 -e EUREKA_URL="http://localhost:8761/eureka" --name product-service-1 codemotiontraining/microservices-product-service 
docker run --rm -d --network host -e PORT=8086 -e EUREKA_URL="http://localhost:8761/eureka" --name product-service-2 codemotiontraining/microservices-product-service 
docker run --rm -d --network host -e PORT=8087 -e EUREKA_URL="http://localhost:8761/eureka" --name product-service-3 codemotiontraining/microservices-product-service 

docker run --rm -d --network host -e PORT=8081 -e EUREKA_URL="http://localhost:8761/eureka" --name shop-service-1 codemotiontraining/microservices-shop-service 
docker run --rm -d --network host -e PORT=8082 -e EUREKA_URL="http://localhost:8761/eureka" --name shop-service-2 codemotiontraining/microservices-shop-service 

docker run --rm -d --network host -e PORT=8080 -e EUREKA_URL="http://localhost:8761/eureka" --name zuul-proxy codemotiontraining/microservices-zuul-proxy 



# chiamiamo i servizi direttamente
curl http://localhost:8085/products/CDF5463GG56
curl http://localhost:8086/products/CDF5463GG56
curl http://localhost:8087/products/CDF5463GG56

curl http://localhost:8081/shops/WEDD321/products/CDF5463GG56
curl http://localhost:8082/shops/WEDD321/products/CDF5463GG56

# chiamiamo il servizio tramite l'api gateway 

# senza credenziali
curl -s -D - http://localhost:8080/api/product-service/products/CDF5463GG56
curl -s -D - http://localhost:8080/api/shop-service/shops/WEDD321/products/CDF5463GG56

# login credenziali sbagliate
curl -s -D - -H 'Content-Type:application/json; charset=utf8' -X POST 'http://localhost:8080/api/shop-service/login' -d '{"username": "shopAdmin", "password": "wrongPassword"}'

# login credenziali corrette
curl -s -D - -H 'Content-Type:application/json; charset=utf8' -X POST 'http://localhost:8080/api/shop-service/login' -d '{"username": "shopAdmin", "password": "secretPassword"}'

# chiamiamo il microservizio tramite il proxy allegando il token di autenticazione
curl -H "Authorization: bearer Fr45dgUDJs8e3hdjke3idhj3hdk8hd" http://localhost:8080/api/product-service/products/CDF5463GG56

# chiamiamo il microservizio tramite il proxy allegando il token di autenticazione
curl -H "Authorization: bearer Fr45dgUDJs8e3hdjke3idhj3hdk8hd" http://localhost:8080/api/shop-service/shops/WEDD321/products/CDF5463GG56

# spegniamo il microservizio dei prodotti
docker stop product-service-1 product-service-2 product-service-3

# chiamiamo il microservizio tramite il proxy allegando il token di autenticazione
curl -H "Authorization: bearer Fr45dgUDJs8e3hdjke3idhj3hdk8hd" http://localhost:8080/api/shop-service/WEDD321/products/CDF5463GG56
