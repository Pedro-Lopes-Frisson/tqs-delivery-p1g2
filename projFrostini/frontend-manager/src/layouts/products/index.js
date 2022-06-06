// @mui material components
import Grid from "@mui/material/Grid";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";

// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import DashboardNavbar from "examples/Navbars/DashboardNavbar";
// import ReportsBarChart from "examples/Charts/BarCharts/ReportsBarChart";
import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Switch,
  TableRow,
  Typography,
} from "@mui/material";

// Data
// import reportsBarChartData from "layouts/dashboard/data/reportsBarChartData";

// Dashboard components

function Products() {
  return (
    <DashboardLayout>
      <DashboardNavbar />
      <MDBox py={3}>
        <Grid container spacing={3}>
          <Grid item xs={12} md={6} lg={3}>
            <MDBox mb={1.5}>
              <Card>
                <CardMedia
                  component="img"
                  height="140"
                  image="https://www.ingredion.com/content/dam/ingredion/other/us/colorblock-images/Solids-Replacement-Icecream-720x560.jpg"
                  alt="vanilla icecream"
                />
                <CardContent>
                  <TableRow>
                    <Typography gutterBottom variant="h5" component="div">
                      Vanilla Ice cream
                    </Typography>
                    <p>
                      In Stock
                      <Switch color="success" />
                    </p>
                  </TableRow>
                  <Typography variant="body2" color="text.secondary">
                    Lizards are a widespread group of squamate reptiles, with over 6,000 species,
                    ranging across all continents except Antarctica
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button size="small">View details</Button>
                </CardActions>
              </Card>
            </MDBox>
          </Grid>
          <Grid item xs={12} md={6} lg={3}>
            <MDBox mb={1.5}>
              <Card>
                <CardMedia
                  component="img"
                  height="140"
                  image="https://www.ingredion.com/content/dam/ingredion/other/us/colorblock-images/Solids-Replacement-Icecream-720x560.jpg"
                  alt="vanilla icecream"
                />
                <CardContent>
                  <TableRow>
                    <Typography gutterBottom variant="h5" component="div">
                      Chocolate Ice cream
                    </Typography>
                    <p>
                      In Stock
                      <Switch color="success" />
                    </p>
                  </TableRow>
                  <Typography variant="body2" color="text.secondary">
                    Lizards are a widespread group of squamate reptiles, with over 6,000 species,
                    ranging across all continents except Antarctica
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button size="small">View details</Button>
                </CardActions>
              </Card>
            </MDBox>
          </Grid>
          <Grid item xs={12} md={6} lg={3}>
            <MDBox mb={1.5}>
              <Card>
                <CardMedia
                  component="img"
                  height="140"
                  image="https://www.ingredion.com/content/dam/ingredion/other/us/colorblock-images/Solids-Replacement-Icecream-720x560.jpg"
                  alt="vanilla icecream"
                />
                <CardContent>
                  <TableRow>
                    <Typography gutterBottom variant="h5" component="div">
                      Strawberry Ice cream
                    </Typography>
                    <p>
                      In Stock
                      <Switch />
                    </p>
                  </TableRow>
                  <Typography variant="body2" color="text.secondary">
                    Lizards are a widespread group of squamate reptiles, with over 6,000 species,
                    ranging across all continents except Antarctica
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button size="small">View details</Button>
                </CardActions>
              </Card>
            </MDBox>
          </Grid>
          <Grid item xs={12} md={6} lg={3}>
            <MDBox mb={1.5}>
              <Card>
                <CardMedia
                  component="img"
                  height="140"
                  image="https://www.ingredion.com/content/dam/ingredion/other/us/colorblock-images/Solids-Replacement-Icecream-720x560.jpg"
                  alt="vanilla icecream"
                />
                <CardContent>
                  <TableRow>
                    <Typography gutterBottom variant="h5" component="div">
                      Cookie Ice cream
                    </Typography>
                    <p>
                      In Stock
                      <Switch color="success" />
                    </p>
                  </TableRow>
                  <Typography variant="body2" color="text.secondary">
                    Lizards are a widespread group of squamate reptiles, with over 6,000 species,
                    ranging across all continents except Antarctica
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button size="small">View details</Button>
                </CardActions>
              </Card>
            </MDBox>
          </Grid>
        </Grid>
        <Grid container spacing={3}>
          <Grid item xs={12} md={6} lg={3}>
            <MDBox mb={1.5}>
              <Card>
                <CardMedia
                  component="img"
                  height="140"
                  image="https://www.ingredion.com/content/dam/ingredion/other/us/colorblock-images/Solids-Replacement-Icecream-720x560.jpg"
                  alt="vanilla icecream"
                />
                <CardContent>
                  <TableRow>
                    <Typography gutterBottom variant="h5" component="div">
                      Vanilla Ice cream
                    </Typography>
                    <p>
                      In Stock
                      <Switch color="success" />
                    </p>
                  </TableRow>
                  <Typography variant="body2" color="text.secondary">
                    Lizards are a widespread group of squamate reptiles, with over 6,000 species,
                    ranging across all continents except Antarctica
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button size="small">View details</Button>
                </CardActions>
              </Card>
            </MDBox>
          </Grid>
          <Grid item xs={12} md={6} lg={3}>
            <MDBox mb={1.5}>
              <Card>
                <CardMedia
                  component="img"
                  height="140"
                  image="https://www.ingredion.com/content/dam/ingredion/other/us/colorblock-images/Solids-Replacement-Icecream-720x560.jpg"
                  alt="vanilla icecream"
                />
                <CardContent>
                  <TableRow>
                    <Typography gutterBottom variant="h5" component="div">
                      Chocolate Ice cream
                    </Typography>
                    <p>
                      In Stock
                      <Switch color="success" />
                    </p>
                  </TableRow>
                  <Typography variant="body2" color="text.secondary">
                    Lizards are a widespread group of squamate reptiles, with over 6,000 species,
                    ranging across all continents except Antarctica
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button size="small">View details</Button>
                </CardActions>
              </Card>
            </MDBox>
          </Grid>
          <Grid item xs={12} md={6} lg={3}>
            <MDBox mb={1.5}>
              <Card>
                <CardMedia
                  component="img"
                  height="140"
                  image="https://www.ingredion.com/content/dam/ingredion/other/us/colorblock-images/Solids-Replacement-Icecream-720x560.jpg"
                  alt="vanilla icecream"
                />
                <CardContent>
                  <TableRow>
                    <Typography gutterBottom variant="h5" component="div">
                      Strawberry Ice cream
                    </Typography>
                    <p>
                      In Stock
                      <Switch />
                    </p>
                  </TableRow>
                  <Typography variant="body2" color="text.secondary">
                    Lizards are a widespread group of squamate reptiles, with over 6,000 species,
                    ranging across all continents except Antarctica
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button size="small">View details</Button>
                </CardActions>
              </Card>
            </MDBox>
          </Grid>
          <Grid item xs={12} md={6} lg={3}>
            <MDBox mb={1.5}>
              <Card>
                <CardMedia
                  component="img"
                  height="140"
                  image="https://www.ingredion.com/content/dam/ingredion/other/us/colorblock-images/Solids-Replacement-Icecream-720x560.jpg"
                  alt="vanilla icecream"
                />
                <CardContent>
                  <TableRow>
                    <Typography gutterBottom variant="h5" component="div">
                      Cookie Ice cream
                    </Typography>
                    <p>
                      In Stock
                      <Switch color="success" />
                    </p>
                  </TableRow>
                  <Typography variant="body2" color="text.secondary">
                    Lizards are a widespread group of squamate reptiles, with over 6,000 species,
                    ranging across all continents except Antarctica
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button size="small">View details</Button>
                </CardActions>
              </Card>
            </MDBox>
          </Grid>
        </Grid>
      </MDBox>
    </DashboardLayout>
  );
}

export default Products;
