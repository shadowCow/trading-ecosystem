import React, { Component } from 'react';
import './App.css';
import {ScatterChart, Scatter, XAxis, YAxis, CartesianGrid, Tooltip, Legend} from 'recharts';
import Spinner from 'react-spinkit';

const latestDataUrl = "http://localhost:8090/latest-data";
const availableTransformsUrl = "http://localhost:8091/available-transforms";
const transformCommandUrl = "http://localhost:8091/perform-transform";

class App extends Component {

  constructor(props) {
    super(props);

    this.state = {
      chartData: {},
      isLoadingChartData: false,
      statusMessage: ''
    };

    this.submitTransformRequest = this.submitTransformRequest.bind(this);
  }

  componentDidMount() {
    this.updateChartData();
  }

  updateChartData() {
    this.setState({
      isLoadingChartData: true,
      statusMessage: 'Getting latest results...'
    });
    fetch(latestDataUrl)
      .then(response => response.json())
      .then(data => {
        this.setState({
          chartData: data,
          statusMessage: 'Latest results retrieved',
          isLoadingChartData: false
        });
      })
      .catch(error => {
        console.log(error);
        this.setState({
          isLoadingChartData: false,
          statusMessage: 'Failed to retrieve latest results'
        });
      });
  }

  submitTransformRequest(request) {
    this.setState({
      statusMessage: 'Submitting transform request...'
    });
    fetch(transformCommandUrl, {
      body: JSON.stringify(request),
      cache: 'no-cache',
      headers: {
        'content-type': 'application/json'
      },
      method: 'POST'
    }).then(response => response.json())
      .then(data => {
        let resultString = data.didSucceed ? 'Success' : 'Failed'
        this.setState({
          statusMessage: resultString + ' - Transform request completed.'
        })
        if (data.didSucceed) {
          this.updateChartData();
        }
      })
      .catch(error => {
        console.error(error);
        this.setState({
          statusMessage: 'Failed - Transform request completed.'
        })
      });
  }

  render() {
    return (
      <div>
        <StatusBar statusMessage={this.state.statusMessage} isWorking={this.state.isLoadingChartData}/>
        <div className="my-flex-container">
          <ChartPanel chartData={this.state.chartData}/>
          <CommandPanel submitTransformRequest={this.submitTransformRequest}/>
        </div>
      </div>

    );
  }
}

class CommandPanel extends Component {
  constructor(props) {
    super(props);

    this.state = {
      availableTransforms: [],
      selectedFeature: '',
      selectedMetric: '',
      isLoadingTransforms: false
    };

    this.handleFeatureChange = this.handleFeatureChange.bind(this);
    this.handleMetricChange = this.handleMetricChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  componentDidMount() {
    this.setState({isLoadingTransforms: true})
    fetch(availableTransformsUrl)
      .then(response => response.json())
      .then(data => {
        this.setState({
          availableTransforms: data.names,
          selectedFeature: data.names[0] || '',
          selectedMetric: data.names[0] || ''
        });
        this.setState({isLoadingTransforms: false});
      })
      .catch(error => {
        console.error(error);
        this.setState({isLoadingTransforms: false});
      });
  }

  handleFeatureChange(event) {
    this.setState({selectedFeature: event.target.value});
  }

  handleMetricChange(event) {
    this.setState({selectedMetric: event.target.value});
  }

  handleSubmit(event) {
    let request = {feature:this.state.selectedFeature, metric:this.state.selectedMetric};
    this.props.submitTransformRequest(request);

    event.preventDefault();
  }

  render() {
    const featureOptions = this.state.availableTransforms.map(at =>
      <option value={at} key={at}>{at}</option>
    );
    const metricOptions = this.state.availableTransforms.map(at =>
      <option value={at} key={at}>{at}</option>
    );

    return (
      <div>
        <h3>Transform!</h3>
        <form onSubmit={this.handleSubmit}>
          <label>
            Feature
            <select value={this.state.selectedFeature} onChange={this.handleFeatureChange}>
              {featureOptions}
            </select>
          </label>
          <label>
            Metric
            <select value={this.state.selectedMetric} onChange={this.handleMetricChange}>
              {metricOptions}
            </select>
          </label>
          <input type="submit" value="Submit" />
        </form>
      </div>
    )
  }
}

class ChartPanel extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <ScatterChart width={600} height={600} margin={{top: 20, right: 20, bottom: 20, left: 20}}>
      	<XAxis dataKey={'x'} type="number" name={this.props.chartData.xName} unit=''/>
      	<YAxis dataKey={'y'} type="number" name={this.props.chartData.yName} unit=''/>
      	<CartesianGrid />
        <Scatter name='Trading!' data={this.props.chartData.dataPoints} fill='#8884d8'/>
      	<Tooltip cursor={{strokeDasharray: '3 3'}}/>
      </ScatterChart>
    );
  }
}

class StatusBar extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    const statusMessage = this.props.statusMessage || 'Ready';
    return (
      <div className="status-bar">
        <span>Status: </span><span>{statusMessage}</span>
        {this.props.isWorking &&
          <Spinner name="circle" color="blue"/>
        }
      </div>
    );
  }
}

export default App;
