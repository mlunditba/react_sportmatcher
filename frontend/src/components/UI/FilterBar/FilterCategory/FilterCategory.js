import React, {Component} from 'react';

import './FilterCategory.css';

class FilterCategory extends Component {
    render() {
        let filter = null;

        switch(this.props.type) {
            case('text'):
                filter = <input type='text' placeholder='Search' styleName='input dark-grey'/>;
                break;
            case('datetime'):
                filter = <input type='datetime-local' styleName='input'/>;
                break;
            case('select'):
                filter = (
                    <select styleName='input'>
                        {this.props.options.map(op => (
                            <option key={op} value={op}>{op}</option>
                        ))}
                    </select>
                );
                break;
            default:
                break;
        }

        return (
            <div styleName='filter'>
                <p styleName='label'>{this.props.label}</p>
                {filter}
            </div>
        );
    }
}

export default FilterCategory;