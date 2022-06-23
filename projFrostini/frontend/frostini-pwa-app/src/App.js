import React from "react";
import "./App.css";
import { Routes, Route } from "react-router-dom";

import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import HomePage from "./pages/HomePage";
import MenuPage from "./pages/MenuPage";
import PurchasePage from "./pages/PurchasePage";
import Layout from "./pages/Layout";
import OrdersHistoryPage from "./pages/OrdersHistoryPage";
import NewOrderPage from "./pages/NewOrderPage";

function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/" element={<HomePage />} />
        <Route path="/menu" element={<MenuPage />} />
        <Route path="/orders" element={<OrdersHistoryPage />} />
        {/* <Route path="/menu/:filter" element={<OrderPage />} /> */}
        <Route path="/purchase/:id" element={<PurchasePage />} />
        {/* <Route path="/purchase" element={<PurchasePage />} /> */}
        <Route path="/order" element={<NewOrderPage />} />
      </Routes>
    </Layout>
  );
}

export default App;
