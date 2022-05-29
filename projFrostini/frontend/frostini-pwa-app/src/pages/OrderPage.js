import "./OrderPage.css";
import QuantityPicker from "../components/QuantityPicker";
import Button from "@mui/material/Button";
import { useNavigate } from "react-router-dom";
import useLocalStorage from "../hooks/use-local-storage";
import { Chip } from "@mui/material";

const data = [
  {
    id: "1",
    title: "Almond Brittle Fudge",
    description:
      "Coconut Milk, Cashew Milk, Vanilla, Sea Salt, Almonds, Cacao Nibs,Chia Seeds, Dairy-free Chocolate Chips, Raw Cane Sugar",
    price: "$4,5",
    img: "https://cdn.shopify.com/s/files/1/0375/0867/7769/products/AlmondBrittleFudge02_c6058f23-8c91-41b5-b1a7-8b93705b5a67_1024x1024@2x.jpg?v=1631857159",
    tags: ["GF", "NF"],
  },
  {
    id: "2",
    title: "Almond Brittle Fudge",
    description:
      "Coconut Milk, Cashew Milk, Vanilla, Sea Salt, Almonds, Cacao Nibs,Chia Seeds, Dairy-free Chocolate Chips, Raw Cane Sugar",
    price: "$4,5",
    img: "https://cdn.shopify.com/s/files/1/0375/0867/7769/products/AlmondBrittleFudge02_c6058f23-8c91-41b5-b1a7-8b93705b5a67_1024x1024@2x.jpg?v=1631857159",
    tags: [],
  },
];

function OrderPage() {
  const [order, setOrder] = useLocalStorage("order", []);

  const handleOrderChange = (product) => {
    const productIndex = order.findIndex((p) => p.id === product.id);

    if (productIndex === -1) {
      if (product.quantity > 0) {
        return setOrder((s) => [...s, product]);
      } else {
        return;
      }
    }

    if (product.quantity > 0) {
      const newOrder = [...order];
      newOrder[productIndex] = product;

      return setOrder(newOrder);
    } else {
      const newOrder = order.filter((p) => p.id === product.id);

      return setOrder(newOrder);
    }
  };
  const navigate = useNavigate();

  return (
    <div className="order-page">
      <div className="header">
        <h1>Order</h1>
        <Button
          variant="contained"
          className="my-order-btn"
          onClick={() => {
            navigate("/purchase");
          }}
        >
          My Order
        </Button>
      </div>
      <div className="products">
        {data.map(({ id, title, description, price, img, tags }) => {
          const quantity = order.find((p) => p.id === id)?.quantity;

          return (
            <div key={id} className="product">
              <img src={img} />
              <div className="product-info">
                <div className="product-title-tags">
                  <p className="product-title">{title}</p>
                  <div className="tags">
                    {tags.map((tag, i) => (
                      <Chip label={tag} className="tags-chip" />
                    ))}
                  </div>
                </div>
                <p className="product-description">{description}</p>
                <div className="product-actions">
                  <span className="product-price">{price}</span>

                  <QuantityPicker
                    onChange={(quantity) =>
                      handleOrderChange({ id, title, price, tags, quantity })
                    }
                    quantity={quantity}
                  />
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default OrderPage;
