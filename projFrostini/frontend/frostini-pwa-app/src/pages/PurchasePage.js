import "./PurchasePage.css";
import * as React from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { Button, Chip, TextField } from "@mui/material";

function createData(icecream, quantity, price, tags) {
  return { icecream, quantity, price, tags };
}

const rows = [
  createData("Frozen yoghurt", 2, 6.0, ["GF", "NF"]),
  createData("Black Forest", 1, 2.3, []),
];

function PurchasePage() {
  // const [order, setOrder] = useLocalStorage("order", []);

  return (
    <div className="purchase-page">
      <h1>My Order</h1>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
          <TableHead className="order-table-header">
            <TableRow>
              <TableCell>Icecream</TableCell>
              <TableCell align="right">Quantity</TableCell>
              <TableCell align="right">Price</TableCell>
              <TableCell align="right">Tags</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.map((row) => (
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
                    {row.tags.map((data, key) => (
                      <Chip label={data} className="tags-chip" />
                    ))}
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <div className="total-price">
        <Chip label="Total: 8.3 €" className="total-price-chip" />
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
