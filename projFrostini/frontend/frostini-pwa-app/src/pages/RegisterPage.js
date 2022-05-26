import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./RegisterPage.css";

function RegisterPage() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [user, loading, error] = /* useAuthState(auth) */ useState("");
    const navigate = useNavigate();

    useEffect(() => {
      if (loading) {
        // maybe trigger a loading screen
        return;
      }
      if (user) navigate("/dashboard");
    }, [user, loading]);

    return (
      <div className="register" id="registerform">
        <h2 id="headerTitle">Register</h2>
        <div className="register__container">
          <div className="row">  
            <label>Name</label>
            <input
                type="text"
                className="register__textBox"
                value={email}
                onChange={(e) => setName(e.target.value)}
                placeholder="Name"
            />
          </div>
          <div className="row">  
            <label>Email</label>
            <input
                type="text"
                className="register__textBox"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="E-mail Address"
            />
          </div>
          <div className="row">  
            <label>Password</label>
            <input
                type="password"
                className="register__textBox"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password"
            />
          </div>
          <div className="row">  
            <label>Confirm Password</label>
            <input
                type="password"
                className="register__textBox"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="Password"
            />
          </div>
          <div className="row" id="button">
            <button
                className="register__btn"
                /* onClick={() => signInWithEmailAndPassword(email, password)} */
            >
                Register
            </button>
          </div>
          <div id="alreadyHasAccount">
            Already have an account? <Link to="/login">Login</Link> now.
          </div>
        </div>
      </div>
    );
}

export default RegisterPage;