/**
 * After the request has been parsed and performed, information is sent
 * back to client (via {@code OutputStream} of the opened socket). This also
 * implies that two internal actions are done by invoking this method: i)
 * perform the request and process data ii) take data from i), wrap them so they
 * are parsable by client
 *
 * Resulting message which should be sent over the network is the return value.
 */
package protocol.request;
