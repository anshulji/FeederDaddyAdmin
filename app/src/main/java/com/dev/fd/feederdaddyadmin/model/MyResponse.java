package com.dev.fd.feederdaddyadmin.model;

import java.util.List;

public class MyResponse {

    public long multicast_id;
    public int success;
    public int failure;
    public int canonical_ids;
    public List<Result> results;

}
