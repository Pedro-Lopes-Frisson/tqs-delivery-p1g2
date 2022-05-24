import "./OrderPage.css";
import QuantityPicker from "../components/QuantityPicker";

function OrderPage() {
  return (
    <div className="order-page">
      <h1>Order</h1>
      <div className="products">
        <div className="product">
          <img src="https://cdn.shopify.com/s/files/1/0375/0867/7769/products/AlmondBrittleFudge02_c6058f23-8c91-41b5-b1a7-8b93705b5a67_1024x1024@2x.jpg?v=1631857159" />
          <div className="product-info">
            <p className="product-title">ALMOND BRITTLE FUDGE</p>
            <p className="product-description">
              Coconut Milk, Cashew Milk, Vanilla, Sea Salt, Almonds, Cacao Nibs,
              Chia Seeds, Dairy-free Chocolate Chips, Raw Cane Sugar
            </p>
            <div className="product-actions">
              <span className="product-price">4.5€</span>

              <QuantityPicker onChange={(q) => console.log(q)} quantity={0} />
            </div>
          </div>
        </div>
        <div className="product">
          <img src="https://cdn.shopify.com/s/files/1/0375/0867/7769/products/AlmondBrittleFudge02_c6058f23-8c91-41b5-b1a7-8b93705b5a67_1024x1024@2x.jpg?v=1631857159" />
          <div className="product-info">
            <p className="product-title">ALMOND BRITTLE FUDGE</p>
            <p className="product-description">
              Coconut Milk, Cashew Milk, Vanilla, Sea Salt, Almonds, Cacao Nibs,
              Chia Seeds, Dairy-free Chocolate Chips, Raw Cane Sugar
            </p>
            <div className="product-actions">
              <span className="product-price">4.5€</span>

              <QuantityPicker quantity={0} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default OrderPage;
