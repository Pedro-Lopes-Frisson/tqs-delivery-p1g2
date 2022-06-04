import {
  Button,
  Card,
  CardActions,
  CardContent,
  Typography,
} from "@mui/material";
import CalendarTodayIcon from "@mui/icons-material/CalendarToday";
import FmdGoodIcon from "@mui/icons-material/FmdGood";
import { useNavigate } from "react-router-dom";
import { useContext, useEffect } from "react";
import "./OrdersHistoryPage.css";
import CheckIcon from "@mui/icons-material/Check";
import AuthContext from "../context/AuthProvider";
import isAuthenticated from "../utils/Authentication";

const json = [
  {
    id: 1,
    date: "27-May-2022",
    address: "Universidade de Aveiro, 3810-193 Aveiro",
    delivered: false,
  },
  {
    id: 2,
    date: "28-May-2022",
    address: "Universidade de Aveiro, 3810-193 Aveiro",
    delivered: true,
  },
];

function OrdersHistoryPage() {
  const navigate = useNavigate();
  const { auth } = useContext(AuthContext);
  const isAuth = isAuthenticated(auth);

  useEffect(() => {
    if (!isAuth) {
      navigate("/login");
    }
  }, [isAuth]);

  return (
    <div className="orders-history-page">
      <h1>ORDERS</h1>
      <div className="orders">
        {json.map((order, key) => (
          <div className="order">
            <Card sx={{ minWidth: 275 }} className="order-card">
              <CardContent>
                <Typography sx={{ mb: 1.5 }} color="#f8c8b9" fontWeight="900">
                  Order #{order.id}
                </Typography>
                <div className="order-info">
                  <div className="order-date">
                    <CalendarTodayIcon />
                    <Typography variant="body2" color="text.secondary">
                      {order.date}
                    </Typography>
                  </div>
                  <div className="order-address">
                    <FmdGoodIcon />
                    <Typography variant="body2" color="text.secondary">
                      {order.address}
                    </Typography>
                  </div>
                </div>
              </CardContent>
              <CardActions>
                <div className="order-details">
                  <Button
                    style={{
                      backgroundColor: "#f8c8b9",
                      color: "#ffffff",
                      fontWeight: "600",
                      fontSize: "12px",
                      border: "0",
                      height: "40px",
                    }}
                    variant="outlined"
                    size="small"
                    className="order-card-btn"
                    onClick={() => {
                      navigate("/purchase");
                    }}
                  >
                    Check More About The Order
                  </Button>
                  {order.delivered && (
                    <div className="deliver-check">
                      <CheckIcon sx={{ color: "#9cb737" }} />
                      <p>Delivered</p>
                    </div>
                  )}
                </div>
              </CardActions>
            </Card>
          </div>
        ))}
      </div>
    </div>
  );
}
export default OrdersHistoryPage;
