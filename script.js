const container = document.querySelector('.container');
const weatherBox = document.querySelector('.weather-box');

const APIKey = '8010b76d2a01e1f2306d901a8e41fe7b';
const city = 'Poole';

fetch(`https://api.openweathermap.org/data/2.5/weather?q=
${city}&units=metric&appid=${APIKey}`).then(response => response.json()).then(json => {
    const temperature = document.querySelector('.weather-box .temperature');

    document.getElementById("temp").innerHTML = `${parseInt(json.main.temp)}<span>°C</span>`;

})



// Check browser cache first, use if there and less than 10 seconds old
if(localStorage.when != null
    && parseInt(localStorage.when) + 10000 > Date.now()) {
    let freshness = Math.round((Date.now() - localStorage.when)/1000) + " second(s)";
    document.getElementById("temp").innerHTML = localStorage.myTemperature;
} else {
    fetch(`https://api.openweathermap.org/data/2.5/weather?q=
${city}&units=metric&appid=${APIKey}`)
        // Convert response string to json object
        .then(response => response.json())
        .then(response => {
// Copy one element of response to our HTML paragraph
            document.getElementById("myTemperature").innerHTML = response.weather_temperature;
// Save new data to browser, with new timestamp
            localStorage.myTemperature = response.weather_temperature + '°';
        })
        .catch(err => {
// Display errors in console
            console.log(err);
        });
}
function getDataFromLocalStorage() {
    const cachedData = localStorage.getItem('temp');
    return cachedData ? JSON.parse(cachedData) : null;
}