import React, { useEffect, useState, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './RegisterPage.css';
import axios from '../api/axios';
import Popup from '../components/Popup';

const REGISTER_URL = '/user';
const EMAIL_REGEX = /^[a-zA-Z0-9]+(?:@[a-zA-Z0-9]+\.)+[A-Za-z]+$/;
/* const PWD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$/; */

function RegisterPage() {
    const userRef = useRef();
    const errRef = useRef();

    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);
    // const [user, loading, error] = /* useAuthState(auth) */ useState('');
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
    }, [name, email, password, confirmPassword]);


    const handleSubmit = async (e) => {
      e.preventDefault();

      if(password === confirmPassword) {

        const v1 = EMAIL_REGEX.test(email);
        const v2 = password.length >= 8;

        if(v1 && v2) {

          await axios.post(`${REGISTER_URL}`, {
            "name": name,
            "pwd": password,
            "email": email
          })
            .then(
              res => {
                if(res.status === 201) {
                  setSuccess(true);
                }

            }).catch(err => {
              if (err.response.status === 0) {
                setError('No server response');
              } else if (err.response.status === 409) {
                setError('User already exists');
              } else {
                setError('Register failed');
              }
              errRef.current.focus();
            })
        } else if (!v1) {
          setError('Invalid email');
          errRef.current.focus();
        } else if (!v2) {
          //setError('Password size must be at least 8 and have lowercase, uppercase, number and special characters');
          setError('Password size must be at least 8');
          errRef.current.focus();
        }
        
      } else {
        setError('Passwords do not match');
        errRef.current.focus();
      }
    }

    return (
      <>
      <div className="register" id="registerform">
        <h2 id="headerTitle">Register</h2>
        <div className="register__container">
          <Popup trigger={success} setTrigger={setSuccess}>
            <h3>Register successfully</h3>
            <p id="success"><Link to="/login">Login</Link> now</p>
          </Popup>
          <form onSubmit={handleSubmit}>
            <div className="row">
              <p ref={errRef} className={error ? "errmsg":"offscrenn"} aria-live="assertive">{error}</p>
            </div>
            <div className="row">  
              <label>Name</label>
              <input
                  type="text"
                  className="register__textBox"
                  value={name}
                  ref={userRef}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="Name"
                  required
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
                  required
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
                  required
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
                  required
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
          </form>
          <div id="alreadyHasAccount">
            Already have an account? <Link to="/login">Login</Link> now.
          </div>
        </div>
      </div>
    </>);
}

export default RegisterPage;