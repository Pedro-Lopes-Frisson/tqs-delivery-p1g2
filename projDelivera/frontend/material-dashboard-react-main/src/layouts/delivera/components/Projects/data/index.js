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
      { Header: "Rider Name", accessor: "Rider_Name", width: "45%", align: "left" },
      { Header: "Avg Time Taken", accessor: "Avg_Time_Taken", align: "center" },
      { Header: "Avg Rating", accessor: "Avg_Rating", width: "10%", align: "left" },
    ],

    rows: [
      {
        Products: <Company image={logoXD} name="Chocolate ice cream" />,
        Rider_Name: (
          <MDTypography variant="caption" color="text" fontWeight="medium">
            Rider 1
          </MDTypography>
        ),
        Avg_Time_Taken: (
          <MDTypography variant="caption" color="text" fontWeight="medium">
            30(min)
          </MDTypography>
        ),
        Avg_Rating: (
          <MDTypography variant="caption" color="text" fontWeight="medium">
            4.5/5
          </MDTypography>
        ),
      },
      {
        Products: <Company image={logoXD} name="Chocolate ice cream" />,
        Rider_Name: (
          <MDTypography variant="caption" color="text" fontWeight="medium">
            Rider 2
          </MDTypography>
        ),
        Avg_Time_Taken: (
          <MDTypography variant="caption" color="text" fontWeight="medium">
            30(min)
          </MDTypography>
        ),
        Avg_Rating: (
          <MDTypography variant="caption" color="text" fontWeight="medium">
            4.5/5
          </MDTypography>
        ),
      },
    ],
  };
}
