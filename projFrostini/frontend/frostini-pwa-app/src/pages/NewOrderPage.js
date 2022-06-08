import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Link } from "react-router-dom";
import "./NewOrderPage.css";
import useLocalStorage from "../hooks/use-local-storage";
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
  RadioGroup,
  Radio,
  FormControl,
  FormControlLabel,
} from "@mui/material";
import AuthContext from '../context/AuthProvider';
import isAuthenticated from '../utils/Authentication';
import Popup from '../components/Popup';
import axios from '../api/axios';

const NEW_ORDER_URL = '/order';

function NewOrderPage() {
  const navigate = useNavigate();
  const { auth } = useContext(AuthContext);
  const isAuth = isAuthenticated(auth);
  const [order, setOrder] = useLocalStorage("order", []);
  const [newAddress, setNewAddress] = useState(false);
  const [address, setAddress] = useLocalStorage('address', []);
  const [state, setState] = useLocalStorage('state', '');
  const [success, setSuccess] = useState(false);

  /* console.log(auth);
  console.log(order); */

  useEffect(() => {
    if(!auth.address) {
      setNewAddress(true);
    }
  });

  useEffect(() => {
    if(!isAuth) {
      navigate('/login');
    }
  }, [isAuth]);

  const totalPrice = order.reduce(
    (sum, item) => item.quantity * item.price + sum,
    0
  );

  const handleSubmit = async (e) => {
    e.preventDefault();

    if(newAddress) {
      setAddress([e.currentTarget['address'].value, e.currentTarget['city'].value, e.currentTarget['zip-code'].value]);
    }

    console.log("POST");
    console.log(body);

    const body =  {
      "addressId": 1,
      "userId": auth.id,
      "orderedProductsList": order.map(product => {
        return {"quantity": product.quantity, "productId": product.id };
      })
    };

    await axios.post(`${NEW_ORDER_URL}`, body)
      .then(
        res => {
          if(res.status === 201) {
            console.log("NEW ORDER");
            setSuccess(true);
            setState('ordered');
          }
  
      }).catch(err => {
        console.log(err.response);
        if (err.response.status === 0) {
          //setError('No server response');
        } else {
          //setError('Register failed');
        }
      })
    
    //navigate('/purchase');
  };

  return (
    <div className="new-order-page">
      <h1>New Order</h1>
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
      <Popup trigger={success} setTrigger={setSuccess}>
        <h3>Order Confirmed</h3>
        <p>Wait for a rider to be attributed</p>
        <p>Go back to the <Link to="/menu">Menu</Link></p>
      </Popup>
      <form onSubmit={handleSubmit}>
          <div className="address-details">
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
                    control={<Radio />}
                    label="Default Address"
                  />
                }
                <div className="alternative-address">
                  <FormControlLabel
                    value={auth.address ? "another-address" : "default-address"}
                    control={<Radio />}
                    onClick={() => setNewAddress(true)}
                    label="Another Address"
                  />
                  <div>
                    {newAddress ? (
                      <div>
                        <TextField id="address" variant="outlined" label="Address" required/>
                        <TextField id="city" variant="outlined" label="City" required/>
                        <TextField id="zip-code" variant="outlined" label="Zip code" required/>
                      </div>
                    ) : null}
                  </div>
                </div>
              </RadioGroup>
            </FormControl>
          </div>
        <Button className="order-btn" variant="contained" type="submit">
          Confirm Order
        </Button>
      </form>
    </div>
  );
}

export default NewOrderPage;
