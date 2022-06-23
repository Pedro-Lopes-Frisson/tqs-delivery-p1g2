import "./MenuPage.css";
import QuantityPicker from "../components/QuantityPicker";
import { useNavigate } from "react-router-dom";
import useLocalStorage from "../hooks/use-local-storage";
import { Chip, InputAdornment, TextField, Button } from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import { useState, useContext, useEffect } from "react";
import AuthContext from "../context/AuthProvider";
import isAuthenticated from "../utils/Authentication";
import axios from '../api/axios';

const PRODUCTS_URL = '/products/available';

/* const data = [
  {
    id: "1",
    title: "Almond Brittle Fudge",
    description:
      "Coconut Milk, Cashew Milk, Vanilla, Sea Salt, Almonds, Cacao Nibs,Chia Seeds, Dairy-free Chocolate Chips, Raw Cane Sugar",
    price: 4.5,
    img: "https://cdn.shopify.com/s/files/1/0375/0867/7769/products/AlmondBrittleFudge02_c6058f23-8c91-41b5-b1a7-8b93705b5a67_1024x1024@2x.jpg?v=1631857159",
    tags: ["GF", "NF"],
  },
  {
    id: "2",
    title: "Chocolate Brittle Fudge",
    description:
      "Coconut Milk, Cashew Milk, Vanilla, Sea Salt, Almonds, Cacao Nibs,Chia Seeds, Dairy-free Chocolate Chips, Raw Cane Sugar",
    price: 4,
    img: "https://cdn.shopify.com/s/files/1/0375/0867/7769/products/AlmondBrittleFudge02_c6058f23-8c91-41b5-b1a7-8b93705b5a67_1024x1024@2x.jpg?v=1631857159",
    tags: [],
  },
]; */

function MenuPage() {
  const { auth } = useContext(AuthContext);
  const isAuth = isAuthenticated(auth);
  const navigate = useNavigate();
  const [order, setOrder] = useLocalStorage('order', []);
  const [search, setSearch] = useState('');
  const [error, setError] = useState('');
  const [data, setData] = useState([]);

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
    item.name.match(new RegExp(search, "i"))
  );

  useEffect(() => {
    if (!isAuth) {
      navigate("/login");
    }
  }, [isAuth]);

  useEffect(() => {
    axios.get(`${PRODUCTS_URL}`)
        .then(
          res => {
            if(res.status == 200) {
              //const data = res.data;
              setData(res.data);
            }

        }).catch(err => {
          setError('Something went wrong');
        })
  }, [data]);

  return (
    <div className="order-page">
      <div className="menu-section">
        <h1>MENU</h1>

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
        <div className="products">
          {filteredData.map(({ id, name, description, price, photoUrl/* , tags */ }) => {
            const quantity = order.find((p) => p.id === id)?.quantity;

            return (
              <div key={id} className="product">
                <img src={photoUrl} alt="icecream" />
                <div className="product-info">
                  <div className="product-title-tags">
                    <p className="product-title">{name}</p>
                    {/* <div className="tags">
                      {tags.map((tag, i) => (
                        <Chip key={i} label={tag} className="tags-chip" />
                      ))}
                    </div> */}
                  </div>
                  <p className="product-description">{description}</p>
                  <div className="product-actions">
                    <span className="product-price">{`${price} €`}</span>

                    <QuantityPicker
                      onChange={(quantity) =>
                        handleOrderChange({ id, name, price, /* tags, */ quantity })
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
      <div className="side-buttons">
        <Button
          variant="contained"
          className="new-order-btn"
          onClick={() => { navigate('/order') }}
        >
          Confirm New Order
        </Button>
        <Button
          variant="contained"
          className="my-order-btn"
          onClick={() => {
            navigate("/orders");
          }}
        >
          My Orders
        </Button>
        <Button
          variant="contained"
          className="my-profile-btn"
          onClick={() => {
            navigate("/profile");
          }}
        >
          My Profile
        </Button>
      </div>
    </div>
  );
}

export default MenuPage;