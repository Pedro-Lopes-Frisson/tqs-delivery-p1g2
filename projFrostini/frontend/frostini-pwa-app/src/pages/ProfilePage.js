import "./ProfilePage.css";
import PersonIcon from "@mui/icons-material/Person";
import EmailIcon from "@mui/icons-material/Email";
import HomeIcon from "@mui/icons-material/Home";
import { Button, Chip, IconButton, TextField } from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const json = {
  name: "John Doe",
  email: "jd@ua.pt",
  address: "",
};

function PurchasePage() {
  const [edit, setEdit] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();

    const body = {
      name: e.currentTarget.name.value,
      email: e.currentTarget.email.value,
      address: e.currentTarget.address.value,
    };

    console.log(body);

    setEdit(false);
  };
  return (
    <div className="profile-page">
      <h1>PROFILE</h1>
      <div className="profile-components">
        <div className="left-section">
          <div className="profile-img">
            <PersonIcon className="profile-icon" />
            {!edit && (
              <IconButton
                className="edit-btn"
                onClick={() => {
                  setEdit(true);
                }}
              >
                <EditIcon className="edit-icon" />
              </IconButton>
            )}
          </div>
          {!edit && (
            <div className="user">
              <p>User</p>
              <Chip className="chip-user-name" label={json["name"]} />
            </div>
          )}
          <Button
            className="check-orders-btn"
            onClick={() => {
              navigate("/orders");
            }}
          >
            Check My Orders
          </Button>
          <Button
            className="logout-btn"
            onClick={() => {
              navigate("/login");
            }}
          >
            Log Out
          </Button>
        </div>
        {edit ? (
          <form className="right-section" onSubmit={handleSubmit}>
            <div className="name-info">
              <PersonIcon className="profile-icon" />
              <div className="text-field-container">
                <TextField id="name" label="Name" variant="standard" />
              </div>
            </div>
            <div className="email-info">
              <EmailIcon className="email-icon" />
              <div className="text-field-container">
                <TextField id="email" label="Email" variant="standard" />
              </div>
            </div>
            <div className="address-info">
              <HomeIcon className="address-icon" />
              <div className="text-field-container">
                <TextField id="address" label="Address" variant="standard" />
              </div>
            </div>
            <div className="edit-btns">
              <Button
                className="edit-btn"
                onClick={() => {
                  setEdit(false);
                }}
              >
                Exit
              </Button>
              <Button className="edit-btn" type="submit">
                Save Changes
              </Button>
            </div>
          </form>
        ) : (
          <div className="right-section">
            <div className="email-info">
              <EmailIcon className="email-icon" />
              <p>{json["email"]}</p>
            </div>
            <div className="address-info">
              <HomeIcon className="address-icon" />
              <p>
                {json["address"] === ""
                  ? "No default address"
                  : json["address"]}
              </p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default PurchasePage;
