import React from 'react';
import logo from './logo.svg';
import './App.css';

import { Link} from '@mui/material'

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        <Link href="/wow">Link</Link>
        {/* <Router>
          <Link component={RouterLink} to="/">
            With prop forwarding
          </Link>
          <br />
          <Link component={LinkBehavior}>Without prop forwarding</Link>
        </Router> */}
      </header>
    </div>
  );
}

export default App;
// import * as React from 'react';
// import ReactDOM from 'react-dom';
// import Button from '@mui/material/Button';

// function App() {
//   return <Button variant="contained"> Olá Mundo</Button>;
// }
// export default App();