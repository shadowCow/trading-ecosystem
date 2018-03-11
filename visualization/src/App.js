import React, { Component } from 'react';
import './App.css';
import {ScatterChart, Scatter, XAxis, YAxis, CartesianGrid, Tooltip, Legend} from 'recharts';

const data = [{x: 100, y: 200, z: 200}, {x: 120, y: 100, z: 260},
                  {x: 170, y: 300, z: 400}, {x: 140, y: 250, z: 280},
                  {x: 150, y: 400, z: 500}, {x: 110, y: 280, z: 200},
                {x: 50, y: 50, z: 500}, {x:10,y:300,z:200},
              {x:30,y:150,z:100}];

class App extends Component {

  constructor(props) {
    super(props);

    this.state = {
      dataPoints: []
    };
  }

  componentDidMount() {
    fetch("http://localhost:8090/latest-data")
      .then(response => response.json())
      .then(data => {
        this.setState({ dataPoints: data.dataPoints });
      });
  }

  render() {
    return (
      <ScatterChart width={600} height={600} margin={{top: 20, right: 20, bottom: 20, left: 20}}>
      	<XAxis dataKey={'x'} type="number" name='stature' unit=''/>
      	<YAxis dataKey={'y'} type="number" name='weight' unit=''/>
      	<CartesianGrid />
        <Scatter name='Trading!' data={this.state.dataPoints} fill='#8884d8'/>
      	<Tooltip cursor={{strokeDasharray: '3 3'}}/>
      </ScatterChart>
    );
  }
}

export default App;
