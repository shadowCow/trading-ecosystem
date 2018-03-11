import React, { Component } from 'react';
import './App.css';
import {ScatterChart, Scatter, XAxis, YAxis, CartesianGrid, Tooltip, Legend} from 'recharts';

class App extends Component {

  constructor(props) {
    super(props);

    this.state = {
      data: {}
    };
  }

  componentDidMount() {
    fetch("http://localhost:8090/latest-data")
      .then(response => response.json())
      .then(data => {
        this.setState({ data: data });
      });
  }

  render() {
    return (
      <ScatterChart width={600} height={600} margin={{top: 20, right: 20, bottom: 20, left: 20}}>
      	<XAxis dataKey={'x'} type="number" name={this.state.data.xName} unit=''/>
      	<YAxis dataKey={'y'} type="number" name={this.state.data.yName} unit=''/>
      	<CartesianGrid />
        <Scatter name='Trading!' data={this.state.data.dataPoints} fill='#8884d8'/>
      	<Tooltip cursor={{strokeDasharray: '3 3'}}/>
      </ScatterChart>
    );
  }
}

export default App;
