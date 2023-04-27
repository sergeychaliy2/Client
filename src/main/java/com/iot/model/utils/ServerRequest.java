package com.iot.model.utils;

import com.iot.model.constants.HttpRequestTypes;
import org.json.simple.JSONObject;

/**
 * @param underPath
 * EndPoint of request. For example: 'https://url:port/articles', where /articles - is endpoint.
 * @param reqType
 * Method of http request. Post or Get. //There`re only 2 methods at the server available.
 * @param postBody
 * Body of post request by json. Can be null.
 */
public record ServerRequest(String underPath, HttpRequestTypes reqType, JSONObject postBody) { }
