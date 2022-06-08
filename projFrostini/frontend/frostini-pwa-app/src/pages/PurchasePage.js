import "./PurchasePage.css";
import * as React from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import {
  Button,
  Chip,
  TextField,
  /* RadioGroup,
  Radio,
  FormControl,
  FormControlLabel, */
  Rating,
} from "@mui/material";
import ShoppingBasketIcon from "@mui/icons-material/ShoppingBasket";
import DeliveryDiningIcon from "@mui/icons-material/DeliveryDining";
import CheckCircleOutlineIcon from "@mui/icons-material/CheckCircleOutline";
import { useNavigate } from 'react-router-dom';
import { useContext, useEffect } from 'react';
import useLocalStorage from "../hooks/use-local-storage";
import AuthContext from '../context/AuthProvider';
import isAuthenticated from '../utils/Authentication';
import axios from '../api/axios';
// import { DatePicker } from '@mui/x-date-pickers/DatePicker';

const NEW_ORDER_URL = '/order';

function PurchasePage() {
  const navigate = useNavigate();
  const { auth } = useContext(AuthContext);
  const isAuth = isAuthenticated(auth);

  const [order, setOrder] = useLocalStorage("order", []);
  const [success, setSuccess] = React.useState(false);

  const [rating, setRating] = React.useState(0);
  const [address, setAddress] = useLocalStorage('address', []);

  const [step, setStep] = React.useState(0);

  const [state, setState] = useLocalStorage('state', '');
  console.log(state);

  const totalPrice = order.reduce(
    (sum, item) => item.quantity * item.price + sum,
    0
  );

  const handleSubmit = async (e) => {
    e.preventDefault();

    const body = {
      cardNumber: e.currentTarget["card-number"].value,
      expirationDate: e.currentTarget["expiration-date"].value,
      cvCode: e.currentTarget["cv-code"].value,
      cardOwner: e.currentTarget["card-owner"].value,
    };

    console.log(body);

    // TODO: get address id
    // address.lenght > 0 ?  address : auth.address


    await axios.post(`${NEW_ORDER_URL}`, {
      'addressId': 1,
      'userId': auth.id,
      'orderedProductsList': order.map(product => {
        return {"quantity": product.quantity, "productId": product.id };
      })
    })
      .then(
        res => {
          if(res.status === 201) {
            setSuccess(true);
            setState("in transit");
          }

      }).catch(err => {
        if (err.response.status === 0) {
          //setError('No server response');
        } else {
          //setError('Register failed');
        }
      })
    
  };

  useEffect(() => {
    if(!isAuth) {
      navigate('/login');
    }
  }, [isAuth]);

  useEffect(() => {
    console.log(state);
    switch(state) {
      case "ordered":
        setStep(1);
        break;
      case "in transit":
        setStep(2);
        break;
      case "delivered":
        setStep(3);
        break;
    }
  });

  return (
    <div className="purchase-page">
      <h1>My Order</h1>
      <div className="order-steps">
        <div
          className="order-state"
          style={{ backgroundColor: step > 0 ? "#9cb737" : "grey" }}
        >
          <ShoppingBasketIcon className="shopping-basket-icon" />
          <p>Ordered</p>
        </div>
        <div
          className="bar"
          style={{ backgroundColor: step > 0 ? "#9cb737" : "grey" }}
        ></div>
        <div
          className="in-transit-state"
          style={{ backgroundColor: step > 1 ? "#9cb737" : "grey" }}
        >
          <DeliveryDiningIcon className="delivery-dining-icon" />
          <p>In Transit</p>
        </div>
        <div
          className="bar"
          style={{ backgroundColor: step > 1 ? "#9cb737" : "grey" }}
        ></div>
        <div
          className="delivered-state"
          style={{
            backgroundColor: step > 2 ? "#9cb737" : "grey",
          }}
        >
          <CheckCircleOutlineIcon className="check-icon" />
          <p>Delivered</p>
        </div>
      </div>

      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
          <TableHead className="order-table-header">
            <TableRow>
              <TableCell className="header-cell">Icecream</TableCell>
              <TableCell className="header-cell" align="right">
                Quantity
              </TableCell>
              <TableCell className="header-cell" align="right">
                Price
              </TableCell>
              <TableCell className="header-cell" align="right">
                Tags
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {order.map((row, key) => (
              <TableRow
                key={key}
                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  {row.title}
                </TableCell>
                <TableCell align="right">{row.quantity}</TableCell>
                <TableCell align="right">{row.price} €</TableCell>
                <TableCell align="right">
                  <div className="tags">
                    {row.tags.map((tag, key) => (
                      <Chip label={tag} key={key} className="tags-chip" />
                    ))}
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <div className="total-price">
        <Chip label={`Total: ${totalPrice}€`} className="total-price-chip" />
      </div>
      {step === 1 && (
        <form onSubmit={handleSubmit}>
          <div className="address-details">
            <h3>Address Details</h3>
            <p>{address.length > 0 ? `${address[0]} ${address[2]} ${address[1]}` : auth.address}</p>
          </div>
          {/* <div className="address-details">
            <h3>Address Details</h3>
            <FormControl>
              <RadioGroup
                aria-labelledby="demo-radio-buttons-group-label"
                defaultValue="default-address"
                name="radio-buttons-group"
              >
                {auth.address ? 
                  <FormControlLabel
                    value="default-address"
                    control={<Radio />}
                    label="Default Address"
                    onClick={() => setNewAddress(false)}
                  />
                  : <FormControlLabel
                    disabled
                    value="default-address"
                    control={<Radio />}
                    label="Default Address"
                  />
                }
                <div className="alternative-address">
                  <FormControlLabel
                    value="another-address"
                    control={<Radio />}
                    onClick={() => setNewAddress(true)}
                    label="Another Address"
                  />
                  <div>
                    {newAddress ? (
                      <div>
                        <TextField id="address" variant="outlined" label="Address"/>
                        <TextField id="city" variant="outlined" label="City"/>
                        <TextField id="zip-code" variant="outlined" label="Zip code"/>
                      </div>
                    ) : null}
                  </div>
                </div>
              </RadioGroup>
            </FormControl>
          </div> */}

          <div className="payment-details">
            <h3>Payment Details</h3>
            <div className="card-info">
              <TextField
                id="card-number"
                label="Card Number"
                multiline
                maxRows={4}
                required
              />
              <div className="sensitive-fields">
                <TextField
                  id="expiration-date"
                  label="Expiration Date"
                  multiline
                  maxRows={4}
                  required
                />
                <TextField id="cv-code" label="CV code" multiline maxRows={4} required/>
              </div>
              <TextField
                id="card-owner"
                label="Card Owner"
                multiline
                maxRows={4}
                required
              />
            </div>
          </div>
          <Button className="payment-btn" variant="contained" type="submit">
            Confirm Payment
          </Button>
        </form>
      )}
      {step > 2 && (
        <div>
          <div className="address-details">
            <h3>Delivery Details</h3>
            <div className="delivery-detail">
              <p>Delivery Address:</p>
              <p>Universidade de Aveiro, 3810-193 Aveiro</p>
            </div>
            <div className="delivery-detail">
              <p>Date:</p>
              <p>28-May-2022 22h45</p>
            </div>
            <div className="delivery-detail">
              <p>Rider:</p>
              <p>John Doe</p>
            </div>
            <div className="delivery-rating">
              <h4>Rider's Rating</h4>
              <Rating
                name="simple-controlled"
                value={rating}
                onChange={(event, newValue) => {
                  setRating(newValue);
                }}
              />
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default PurchasePage;
