import React from 'react';
import './Popup.css';
import Close from '@mui/icons-material/Close';

function Popup(props) {
    return ( props.trigger ? (
    <>
        <div className="popup-container">
            <div className="popup-inner">
                <button className="close-btn" onClick={() => props.setTrigger(false)}>
                    <Close />
                </button>
                { props.children }
            </div>
        </div>
    </>) : "");
}

export default Popup;