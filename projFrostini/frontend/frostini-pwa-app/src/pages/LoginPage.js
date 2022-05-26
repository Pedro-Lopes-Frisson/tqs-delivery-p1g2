import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./LoginPage.css";

function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
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
      <div className="login" id="loginform">
        <h2 id="headerTitle">Login</h2>
        <div className="login__container">
          <div className="row">  
            <label>Email</label>
            <input
                type="text"
                className="login__textBox"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="E-mail Address"
            />
          </div>
          <div className="row">  
            <label>Password</label>
            <input
                type="password"
                className="login__textBox"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password"
            />
          </div>
          <div className="row" id="button">
            <button
                className="login__btn"
                /* onClick={() => signInWithEmailAndPassword(email, password)} */
            >
                Login
            </button>
          </div>
          <div id="alternativeLogin">
            <label>Or sign in with:</label>
            <div id="googleLogin">
                <button className="login__btn login__google" id="googleIcon" /* onClick={signInWithGoogle} */ >
                    {/* Login with Google */} 
                <img src="https://cdn-icons-png.flaticon.com/32/300/300221.png"></img>
                </button>
            </div>
          </div>
          {/* <div id="resetPassword">
            <Link to="/reset">Forgot Password</Link>
          </div> */}
          <div id="newAccount">
            Don't have an account? <Link to="/register">Register</Link> now.
          </div>
        </div>
      </div>
    );
}

export default LoginPage;