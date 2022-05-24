import React from 'react';
import './App.css';
import { Routes, Route } from "react-router-dom";

import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import HomePage from "./components/HomePage";
import OrderPage from "./components/OrderPage";
import PurchasePage from "./components/PurchasePage";



function App() {
  return (
    <Routes>
      <Route
        path="/login"
        element={<LoginPage/>}
      />
        <Route
         path="/register"
         element={ <RegisterPage/>}
       />
      <Route
        path="/"
        element={<HomePage/>}
      ></Route>
      <Route
        path="/order"
        element={<OrderPage/>}
      />
      <Route
        path="/purchase"
        element={<PurchasePage/>}
      />
    </Routes>
  );
}

export default App;
