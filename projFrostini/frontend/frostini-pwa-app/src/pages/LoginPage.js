import React, { useEffect, useRef, useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './LoginPage.css';
import AuthContext from '../context/AuthProvider';
import axios from '../api/axios';

const LOGIN_URL = '/user';

function LoginPage() {
    const { setAuth } = useContext(AuthContext);
    const userRef = useRef();
    const errRef = useRef();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    //const [user, loading, error] = /* useAuthState(auth) */ useState('');
    const navigate = useNavigate();

    /* useEffect(() => {
      if (loading) {
        // maybe trigger a loading screen
        return;
      }
      if (user) navigate('/dashboard');
    }, [user, loading]); */

    useEffect(() => {
      userRef.current.focus();
    }, []);

    useEffect(() => {
      setError('');
    }, [email, password]);

    const handleSubmit = async (e) => {
      e.preventDefault();

      await axios.get(`${LOGIN_URL}/${email}`)
        .then(
          res => {
            if(res.status == 200) {
              const data = res.data;

              if(password == data.password) {
                const id = data.id;
                const name = data.name;
                const address = data.address;
                const order = data.order;
                setAuth({ id, email, password, name, address, order });
                
                if(!data.admin) {
                  navigate('/menu');
                } else {
                  // TODO: manager page
                }

              } else {
                  setError('Invalid login');
                  errRef.current.focus();
              }
            }

        }).catch(err => {
          if(err.response.status == 0) {
            setError('No server response');
          } else if (err.response.status == 400) {
            setError('Wrong email or password');
          } else if (err.response.status == 401) {
            setError('Unauthorized');
          } else {
            setError('Login failed');
          }
          errRef.current.focus();
        })
        

    }

    return (
      <>
      <div className="login" id="loginform">
        <h2 id="headerTitle">Login</h2>
        <div className="login__container">
          <form onSubmit={handleSubmit}>
            <div className="row">
              <p id="errorMessage" ref={errRef} className={error ? "errmsg":"offscrenn"} aria-live="assertive">{error}</p>
            </div>
            <div className="row">  
              <label>Email</label>
              <input
                  type="text"
                  className="login__textBox"
                  value={email}
                  ref={userRef}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="E-mail Address"
                  required
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
                  required
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
          </form>
          {/* <div id="alternativeLogin">
            <label>Or sign in with:</label>
            <div id="googleLogin">
                <button className="login__btn login__google" id="googleIcon" onClick={signInWithGoogle} >
                    Login with Google
                <img src="https://cdn-icons-png.flaticon.com/32/300/300221.png"></img>
                </button>
            </div>
          </div> */}
          {/* <div id="resetPassword">
            <Link to="/reset">Forgot Password</Link>
          </div> */}
          <div id="newAccount">
            Don't have an account? <Link to="/register">Register</Link> now.
          </div>
        </div>
      </div>
    </>);
}

export default LoginPage;