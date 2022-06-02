import "./MenuPage.css";
import QuantityPicker from "../components/QuantityPicker";
import Button from "@mui/material/Button";
import { useNavigate } from "react-router-dom";
import useLocalStorage from "../hooks/use-local-storage";
import { Chip, InputAdornment, TextField } from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import { useState, useContext, useEffect } from "react";
import AuthContext from '../context/AuthProvider';
import isAuthenticated from '../utils/Authentication';

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
    title: "Chocolate Brittle Fudge",
    description:
      "Coconut Milk, Cashew Milk, Vanilla, Sea Salt, Almonds, Cacao Nibs,Chia Seeds, Dairy-free Chocolate Chips, Raw Cane Sugar",
    price: "$4,5",
    img: "https://cdn.shopify.com/s/files/1/0375/0867/7769/products/AlmondBrittleFudge02_c6058f23-8c91-41b5-b1a7-8b93705b5a67_1024x1024@2x.jpg?v=1631857159",
    tags: [],
  },
];

function MenuPage() {
  const { auth } = useContext(AuthContext);
  const isAuth = isAuthenticated(auth);
  const navigate = useNavigate();
  const [order, setOrder] = useLocalStorage("order", []);
  const [search, setSearch] = useState("");

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

  const handleChange = (e) => {
    setSearch(e.target.value);
  };

  const filteredData = data.filter((item) =>
    item.title.match(new RegExp(search, "i"))
  );

  useEffect(() => {
    if(!isAuth) {
      navigate('/login');
    }
  }, [isAuth]);

  return (
    <div className="order-page">
      {/* <div className="header"> */}
      <h1>Menu</h1>
      <div className="header">
        <div className="search-bar">
          <TextField
            value={search}
            id="search"
            label="Search for products"
            variant="standard"
            onChange={handleChange}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }}
          />
        </div>
        <Button
          variant="contained"
          className="my-order-btn"
          onClick={() => {
            navigate("/orders");
          }}
        >
          My Orders
        </Button>
      </div>
      {/* </div> */}
      <div className="products">
        {filteredData.map(({ id, title, description, price, img, tags }) => {
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

export default MenuPage;
