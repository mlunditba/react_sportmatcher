import React from 'react';

import './Button.css';

const button = (props) => {
    const appliedStyles = ['button', props.btnType].join(' ');
    
    return <button styleName={appliedStyles}>{props.children}</button>
};

export default button;