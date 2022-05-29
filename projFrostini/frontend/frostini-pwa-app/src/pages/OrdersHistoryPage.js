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
import "./OrdersHistoryPage.css";

function OrdersHistoryPage() {
  const navigate = useNavigate();
  return (
    <div className="orders-history-page">
      <h1>Orders</h1>
      <div className="orders">
        <div className="order">
          <Card sx={{ minWidth: 275 }}>
            <CardContent>
              <Typography sx={{ mb: 1.5 }} color="text.secondary">
                Order #1
              </Typography>
              <div>
                <div className="order-date">
                  <CalendarTodayIcon />
                  <Typography variant="body2">27-May-2022</Typography>
                </div>
                <div className="order-address">
                  <FmdGoodIcon />
                  <Typography variant="body2">
                    Universidade de Aveiro, 3810-193 Aveiro
                  </Typography>
                </div>
              </div>
            </CardContent>
            <CardActions>
              <Button
                size="small"
                onClick={() => {
                  navigate("/purchase");
                }}
              >
                Check More About The Order
              </Button>
            </CardActions>
          </Card>
        </div>
      </div>
    </div>
  );
}

export default OrdersHistoryPage;
