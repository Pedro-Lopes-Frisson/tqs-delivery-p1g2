##################################
#             USERS
##################################
curl -X 'POST' \
  'http://localhost:8083/api/v1/user' \
  -H 'Accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Pedro",
  "pwd": "pedrolopes",
  "email": "pdfl@ua.pt"
}'
curl -X 'POST' \
  'http://localhost:8083/api/v1/user' \
  -H 'Accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Raquel",
  "pwd": "raquelferreira",
  "email": "raquel@ua.pt"
}'
curl -X 'POST' \
  'http://localhost:8083/api/v1/user' \
  -H 'Accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Sophie",
  "pwd": "Sophiepousinho",
  "email": "sophie@ua.pt"
}'
curl -X 'POST' \
  'http://localhost:8083/api/v1/user' \
  -H 'Accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Patricia",
  "pwd": "patriciadias",
  "email": "patricia@ua.pt"
}'

##################################
#        ADDRESSES
##################################


curl -X 'POST' \
  'http://localhost:8083/api/v1/addresses' \
  -H 'Accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "userId": 1,
  "latitude": 0,
  "longitude": 10
}'
curl -X 'POST' \
  'http://localhost:8083/api/v1/addresses' \
  -H 'Accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "userId": 2,
  "latitude": 10,
  "longitude": 10
}'
curl -X 'POST' \
  'http://localhost:8083/api/v1/addresses' \
  -H 'Accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "userId": 3,
  "latitude": 10.10,
  "longitude": 10
}'
curl -X 'POST' \
  'http://localhost:8083/api/v1/addresses' \
  -H 'Accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "userId": 4,
  "latitude": 10.11,
  "longitude": 10.12
}'


################################
#            Orders
#################################

#curl -X 'POST' \
#  'http://localhost:8083/api/v1/order' \
#  -H 'Accept: */*' \
#  -H 'Content-Type: application/json' \
#  -d '{
#  "addressId": 3,
#  "userId": 1,
#  "orderedProductsList": [
#    {
#      "quantity": 1,
#      "productId": 5
#    },  {
#      "quantity": 12,
#      "productId": 6
#    },  {
#      "quantity": 13,
#      "productId": 7
#    }
#  ]
#}'






#################################
#           Product
#################################
curl -X 'POST' 'http://localhost:8083/api/v1/products' \
  -H 'Accept: */*' -H 'Content-Type: application/json' \
  -d '{
  "price": 4.5,
  "stockQuantity": 1000,
  "name": "almond brittle fudge",
  "description": "coconut milk, cashew milk, vanilla, sea salt, almonds, cacao nibs,chia seeds, dairy-free chocolate chips, raw cane sugar",
  "photoUrl": "https://cdn.shopify.com/s/files/1/0375/0867/7769/products/AlmondBrittleFudge02_c6058f23-8c91-41b5-b1a7-8b93705b5a67_1024x1024@2x.jpg?v=1631857159"
}'

curl -X 'POST' 'http://localhost:8083/api/v1/products' \
  -H 'Accept: */*' -H 'Content-Type: application/json' \
  -d '{
  "price": 4,
  "stockQuantity": 1000,
  "name": "chocolate brittle fudge",
  "description": "coconut milk, cashew milk, vanilla, sea salt, almonds, cacao nibs,chia seeds, dairy-free chocolate chips, raw cane sugar",
  "photoUrl": "https://cdn.shopify.com/s/files/1/0375/0867/7769/products/SEPTEMBERCONTENT_3_1024x1024@2x.png?v=1631857159"
}'

curl -X 'POST' 'http://localhost:8083/api/v1/products' \
  -H 'Accept: */*' -H 'Content-Type: application/json' \
  -d '{
  "price": 5,
  "stockQuantity": 1000,
  "name": "nutella ice cream",
  "description": "egg free nutella ice cream with hazelnut",
  "photoUrl": "https://www.chocolatemoosey.com/wp-content/uploads/2012/09/nutella-ice-cream-no-cook-egg-free-7715.jpg"
}'