import * as React from "react";
import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import Typography from "@mui/material/Typography";
import "./MenuPage.css";
import { useParams, useNavigate } from "react-router-dom";

function MenuPage() {
  const navigate = useNavigate();
  const json = [
    {
      category: "All",
      slug: "all",
      image:
        "https://s7g10.scene7.com/is/image/tetrapak/raspberry-icecream-in-bowl?wid=600&hei=338&fmt=jpg&resMode=sharp2&qlt=85,0&op_usm=1.75,0.3,2,0",
    },
    {
      category: "Gluten Free",
      slug: "gf",
      image:
        "https://s7g10.scene7.com/is/image/tetrapak/raspberry-icecream-in-bowl?wid=600&hei=338&fmt=jpg&resMode=sharp2&qlt=85,0&op_usm=1.75,0.3,2,0",
    },
    {
      category: "Nut Free",
      slug: "nf",
      image:
        "https://s7g10.scene7.com/is/image/tetrapak/raspberry-icecream-in-bowl?wid=600&hei=338&fmt=jpg&resMode=sharp2&qlt=85,0&op_usm=1.75,0.3,2,0",
    },
  ];

  return (
    <div className="menu-page">
      <h1>Menu</h1>
      <div className="options">
        {json.map((data, key) => (
          <Card
            key={key}
            className="card"
            onClick={() => {
              navigate(`/menu/${data.slug}`);
            }}
          >
            <Box className="card-box">
              <CardContent className="card-content">
                <Typography component="div" variant="h5">
                  {data.category}
                </Typography>
              </CardContent>
            </Box>
            <CardMedia
              component="img"
              className="card-media"
              image={data.image}
              alt="all"
            />
          </Card>
        ))}
      </div>
    </div>
  );
}

export default MenuPage;
