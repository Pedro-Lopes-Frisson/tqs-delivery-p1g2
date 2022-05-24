import { useState } from "react";
import "./QuantityPicker.css";

const QuantityPicker = ({ quantity, onChange }) => {
  const [state, setState] = useState(quantity);

  return (
    <div className="quantity-picker">
      <button
        className="quantity-picker-btn"
        onClick={() => {
          if (state > 0) {
            const newState = state - 1;
            setState(newState);
            onChange(newState);
          }
        }}
      >
        -
      </button>
      <span>{state}</span>
      <button
        className="quantity-picker-btn"
        onClick={() => {
          const newState = state + 1;
          setState(newState);
          onChange(newState);
        }}
      >
        +
      </button>
    </div>
  );
};

export default QuantityPicker;
