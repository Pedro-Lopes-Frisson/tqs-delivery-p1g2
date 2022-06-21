curl -X 'POST' 'http://localhost:8083/api/v1/products' \
  -H 'accept: */*' -H 'Content-Type: application/json' \
  -d '{
  "price": 4.5,
  "stockQuantity": 1000,
  "name": "Almond Brittle Fudge",
  "description": "Coconut Milk, Cashew Milk, Vanilla, Sea Salt, Almonds, Cacao Nibs,Chia Seeds, Dairy-free Chocolate Chips, Raw Cane Sugar",
  "photoUrl": "https://cdn.shopify.com/s/files/1/0375/0867/7769/products/AlmondBrittleFudge02_c6058f23-8c91-41b5-b1a7-8b93705b5a67_1024x1024@2x.jpg?v=1631857159"
}'

curl -X 'POST' 'http://localhost:8083/api/v1/products' \
  -H 'accept: */*' -H 'Content-Type: application/json' \
  -d '{
  "price": 4,
  "stockQuantity": 1000,
  "name": "Chocolate Brittle Fudge",
  "description": "Coconut Milk, Cashew Milk, Vanilla, Sea Salt, Almonds, Cacao Nibs,Chia Seeds, Dairy-free Chocolate Chips, Raw Cane Sugar",
  "photoUrl": "https://cdn.shopify.com/s/files/1/0375/0867/7769/products/SEPTEMBERCONTENT_3_1024x1024@2x.png?v=1631857159"
}'

curl -X 'POST' 'http://localhost:8083/api/v1/products' \
  -H 'accept: */*' -H 'Content-Type: application/json' \
  -d '{
  "price": 5,
  "stockQuantity": 1000,
  "name": "Nutella Ice Cream",
  "description": "Egg free Nutella Ice Cream with hazelnut",
  "photoUrl": "https://www.chocolatemoosey.com/wp-content/uploads/2012/09/Nutella-Ice-Cream-No-Cook-Egg-Free-7715.jpg"
}'