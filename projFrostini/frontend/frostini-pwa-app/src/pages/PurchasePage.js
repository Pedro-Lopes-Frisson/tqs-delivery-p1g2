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
  RadioGroup,
  Radio,
  FormControl,
  FormControlLabel,
} from "@mui/material";

const json = [
  { icecream: "Frozen yoghurt", quantity: 2, price: 6.0, tags: ["GF", "NF"] },
  { icecream: "Black Forest", quantity: 1, price: 2.3, tags: [] },
];

// function getTotal() {
//   var total = 0;

//   json.map((row) => (total += row.price));

//   return total;
// }

function PurchasePage() {
  const [newAddress, setNewAddress] = React.useState(false);
  // const [order, setOrder] = useLocalStorage("order", []);

  return (
    <div className="purchase-page">
      <h1>My Order</h1>
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
            {json.map((row) => (
              <TableRow
                key={row.name}
                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  {row.icecream}
                </TableCell>
                <TableCell align="right">{row.quantity}</TableCell>
                <TableCell align="right">{row.price} €</TableCell>
                <TableCell align="right">
                  <div className="tags">
                    {row.tags.map((tag, key) => (
                      <Chip label={tag} className="tags-chip" />
                    ))}
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <div className="total-price">
        {/* TODO: get sum of product prices */}
        <Chip label="Total: x €" className="total-price-chip" />
      </div>
      <div className="address-details">
        <h3>Address Details</h3>
        <FormControl>
          <RadioGroup
            aria-labelledby="demo-radio-buttons-group-label"
            defaultValue="default-address"
            name="radio-buttons-group"
          >
            <FormControlLabel
              value="default-address"
              control={<Radio />}
              label="Default Address"
              onClick={() => setNewAddress(false)}
            />
            <div className="alternative-address">
              <FormControlLabel
                value="another-address"
                control={<Radio />}
                onClick={() => setNewAddress(true)}
                label="Another Address"
              />
              <div>
                {newAddress ? (
                  <TextField id="outlined-basic" variant="outlined" />
                ) : null}
              </div>
            </div>
          </RadioGroup>
        </FormControl>
      </div>
      <div className="payment-details">
        <h3>Payment Details</h3>
        <div className="card-info">
          <TextField
            id="card-number"
            label="Card Number"
            multiline
            maxRows={4}
            value="Valid Card Number"
            onChange={() => {}}
          />
          <div className="sensitive-fields">
            <TextField
              id="expiration-date"
              label="Expiration Date"
              multiline
              maxRows={4}
              value="MM/YYYY"
              onChange={() => {}}
            />
            <TextField
              id="cv-code"
              label="CV code"
              multiline
              maxRows={4}
              value="CVC"
              onChange={() => {}}
            />
          </div>
          <TextField
            id="card-owner"
            label="Card Owner"
            multiline
            maxRows={4}
            value="Card Owner Name"
            onChange={() => {}}
          />
        </div>
      </div>

      <Button className="payment-btn" variant="contained">
        Confirm Payment
      </Button>
    </div>
  );
}

export default PurchasePage;
