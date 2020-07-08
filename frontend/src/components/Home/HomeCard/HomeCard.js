import React from 'react';

import './HomeCard.css';
import Button from '../../UI/Button/Button';

const homeCard = (props) => {
    let card;
    if(props.imgLeft) {
        card = (
            <div data-aos="fade-left" styleName='card'>
                <img styleName='img gradient-right' src={props.imageUrl} alt={props.imageAlt} />
                <div styleName='body left-align'>
                    <h3 styleName='title'>{props.title}</h3>
                    <p styleName='body-text'>{props.children}</p>
                    <div styleName='btn'>
                        <Button btnType='blue'>{props.btnTxt}</Button>
                    </div>
                </div>
            </div>
        );
    } else {
        card = (
            <div data-aos="fade-right" styleName='card'>
                <div styleName='body right-align'>
                    <h3 styleName='title'>{props.title}</h3>
                    <p styleName='body-text'>{props.children}</p>
                    <div styleName='btn btn-right-align'>
                        <Button btnType='blue'>{props.btnTxt}</Button>
                    </div>
                </div>
                <img styleName='img gradient-left' src={props.imageUrl} alt={props.imageAlt} />
            </div>
        );
    }
    
    return card;
}

export default homeCard;