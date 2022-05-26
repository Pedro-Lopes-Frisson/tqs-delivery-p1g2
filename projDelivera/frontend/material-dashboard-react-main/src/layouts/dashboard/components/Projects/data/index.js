/* eslint-disable react/prop-types */
/* eslint-disable react/function-component-definition */
/**
=========================================================
* Material Dashboard 2 React - v2.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-react
* Copyright 2022 Creative Tim (https://www.creative-tim.com)

Coded by www.creative-tim.com

 =========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*/

// @mui material components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDAvatar from "components/MDAvatar";

// Images
import logoXD from "assets/images/small-logos/logo-xd.svg";

export default function data() {
  const Company = ({ image, name }) => (
    <MDBox display="flex" alignItems="center" lineHeight={1}>
      <MDAvatar src={image} name={name} size="sm" />
      <MDTypography variant="button" fontWeight="medium" ml={1} lineHeight={1}>
        {name}
      </MDTypography>
    </MDBox>
  );

  return {
    columns: [
      { Header: "Products", accessor: "Products", width: "45%", align: "left" },
      { Header: "Revenue", accessor: "Revenue", width: "10%", align: "left" },
      { Header: "Quantity Sold", accessor: "Quantity_Sold", align: "center" },
    ],

    rows: [
      {
        Products: <Company image={logoXD} name="Chocolate ice cream" />,
        Revenue: (
          <MDTypography variant="caption" color="text" fontWeight="medium">
            $14,000
          </MDTypography>
        ),
        Quantity_Sold: (
          <MDTypography variant="caption" color="text" fontWeight="medium">
            14,000
          </MDTypography>
        ),
      },
      {
        Products: <Company image={logoXD} name="Vanilla ice cream" />,
        Revenue: (
          <MDTypography variant="caption" color="text" fontWeight="medium">
            $14,000
          </MDTypography>
        ),
        Quantity_Sold: (
          <MDTypography variant="caption" color="text" fontWeight="medium">
            14,000
          </MDTypography>
        ),
      },
    ],
  };
}
