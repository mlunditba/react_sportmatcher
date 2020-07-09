import React, {Component} from 'react';

import './FilterCategory.css';

class FilterCategory extends Component {
    render() {
        let filter = null;

        switch(this.props.type) {
            case('text'):
                filter = <input type='text'/>;
                break;
            case('datetime'):
                filter = <input type='datetime-local'/>;
                break;
            case('select'):
                filter = (
                    <select>
                        {this.props.options.map(op => (
                            <option value={op}>{op}</option>
                        ))}
                    </select>
                );
                break;
        }

        return (
            <div>
                <p>{this.props.label}</p>
                {filter}
            </div>
        );
    }
}

export default FilterCategory;